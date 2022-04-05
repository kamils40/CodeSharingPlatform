package platform;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


import java.util.ArrayList;
import java.util.List;



@Controller
public class CodeAPIController {

    @Autowired //auto wire repository to have an instance of it
    private CodeRepository repository;


    @GetMapping("/api/code/{UUID}")
    @ResponseBody
    public Code getCode(@PathVariable String UUID) {
        filterDataBase();
        Code searchedCode;
        try {
            searchedCode = repository.findById(UUID).get();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "ID not found");
        }
        if(searchedCode.getRestrictionType() == Restriction.VIEW_RESTRICTION
           || searchedCode.getRestrictionType() == Restriction.FULL_RESTRICTION ) {
            searchedCode.reduceViews();
            repository.save(searchedCode);
        }
        return searchedCode;
    }

    @PostMapping("/api/code/new")
    @ResponseBody
    public JsonIdBody postAPINewCode(@RequestBody CodeWithoutDate code) {
        //create new instance of Code as we are adding new snippet
        Code newCode = new Code(code.getCode(),code.getViews(),code.getTime());
        newCode = repository.save(newCode);       // add code to the database
        return new JsonIdBody(newCode.getId());  // return the id as JSON
    }

    @GetMapping("/api/code/latest")
    @ResponseBody
    public List<Code>  getRecentCodes() {
        List<Code> allCodesFromDB = (List<Code>) repository.findAll();
        //filter top 10 recent codes and reverse so the most recent is on the top
        return reverseList(getTenLatestCodesWithoutRestriction(allCodesFromDB));
    }


    @GetMapping(value = "/code/{UUID}")
    public String getWebCode (Model model, @ModelAttribute Code code, @PathVariable String UUID) {
        Code searchedCode;
        filterDataBase();
        try {
            searchedCode = repository.findById(UUID).get();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "ID not found");
        }
        model.addAttribute("snippet", searchedCode);
        //if else to return proper HTML pattern
        if(searchedCode.getRestrictionType() == Restriction.NO_RESTRICTION) {
            return "code";
        } else if (searchedCode.getRestrictionType() == Restriction.TIME_RESTRICTION) {
            return "CodeWithTimeRestriction";
        } else if (searchedCode.getRestrictionType() == Restriction.VIEW_RESTRICTION) {
            searchedCode.reduceViews();
            repository.save(searchedCode);
            return "CodeWithViewRestriction";
       }
        searchedCode.reduceViews();
        repository.save(searchedCode);
        return "CodeWithFullRestriction";

    }



    @GetMapping("/code/latest")
    public String getMostRecentSnippetsWeb(Model model) {
        //get codes from DB
        List<Code> allCodesFromDB = (List<Code>) repository.findAll();
        //filter top 10 recent codes and reverse so the most recent is on the top
        //and pass them to dynamic html via model
        model.addAttribute("snippets",reverseList(getTenLatestCodesWithoutRestriction(allCodesFromDB)));
        return "latest";
    }
    // returns 10 latest code snippets without any restriction from list
    private List<Code> getTenLatestCodesWithoutRestriction(List<Code> codeList) {
        List<Code> unrestrictedList = new ArrayList<>();
        //filter list for unrestricted elements
        for(Code c : codeList) {
             if (c.getRestrictionType().equals(Restriction.NO_RESTRICTION)) {
                 unrestrictedList.add(c);
             }
        }
        if (unrestrictedList.size() < 10) {
            return unrestrictedList; //if less than 10 just return list
        } else {
            return unrestrictedList.subList(unrestrictedList.size() -10, unrestrictedList.size());
            //else return 10 most recent snippets
        }
    }

    private List<Code> reverseList(List<Code> toReverse) { //method to reverse list
       List<Code> reversedList = new ArrayList<>();
        for(int i = toReverse.size() -1 ; i >= 0; i--) { //iterate over list starting from last element
            reversedList.add(toReverse.get(i)); //add element to reversed list
        }
        return reversedList;
    }
    private void filterDataBase() {
        //filtering database to check if any of the snippets didn't expire
        //the code expire if any of the restrictions hit threshold e.g time passed or code
        //was visited exact number of views
        List<Code> codeList = (List<Code>) repository.findAll();
        for(Code c : codeList) {
            if(!c.canCodeBeShowed()) {
                repository.deleteById(c.getId());
            }
        }
    }
}
