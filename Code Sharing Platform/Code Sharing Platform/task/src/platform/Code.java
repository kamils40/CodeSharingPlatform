package platform;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.tomcat.jni.Local;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Formatter;


@Entity
@Table(name = "snippets")
public class Code {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "code_id")
    private String id;
    @Lob
    @Column
    private String code;
    @Column
    private String date;
    @Column
    private int time;
    @Column
    private int views;
    @Enumerated(EnumType.STRING)
    private Restriction restrictionType;
    private long expiredTime;

    //formatter to DateTime pattern without nanoseconds
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Code(String s, int views, int time) {

        this.code = s;
        date = LocalDateTime.now().format(FORMATTER);
        this.views = views;
        this.time = time;
        this.expiredTime = 0;
        setRestrictionType();
    }

    public Code(){

    }
    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate() {
        this.date =  LocalDateTime.now().format(FORMATTER);
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getTime() {
        return this.time;
    }

    public void setViews(int view) {
        this.views = view;
    }

    public int getViews() {
        return this.views;
    }
    @JsonIgnore
    public void setRestrictionType() {
        if (this.views <= 0 && this.time <= 0) {
            this.views = 0;
            this.time = 0;
            this.restrictionType = Restriction.NO_RESTRICTION;
        } else if (this.views > 0 && this.time > 0) {
            this.restrictionType = Restriction.FULL_RESTRICTION;
        } else if ( this.views > 0) {
            this.time = 0;
            this.restrictionType = Restriction.VIEW_RESTRICTION;
        } else if (this.time > 0) {
            this.views = 0;
            this.restrictionType = Restriction.TIME_RESTRICTION;
        }
    }
    public void reduceViews() {
        this.views -= 1;
    }
    public boolean CodeTimeVisibilityOver() {
       // LocalDateTime now = LocalDateTime.now();
       // LocalDateTime creationTime = LocalDateTime.parse(date,FORMATTER);
       // LocalDateTime expireDate = creationTime.plusSeconds(time);
        updateTime();
        return this.time == 0;
    }
    public boolean canCodeBeShowed() {
        if (restrictionType == Restriction.NO_RESTRICTION) {
            return true;
        } else if (restrictionType == Restriction.VIEW_RESTRICTION) {
            return !(views == 0);
        } else if (restrictionType == Restriction.TIME_RESTRICTION) {
            return !CodeTimeVisibilityOver();
        }
        return (views != 0 && !CodeTimeVisibilityOver());
    }
    public void updateTime() {
        long totalTimeOfVisibility = this.expiredTime + (long) this.time;
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime creationTime = LocalDateTime.parse(date,FORMATTER);
        expiredTime = ChronoUnit.SECONDS.between(creationTime,now);
        if(expiredTime > totalTimeOfVisibility) {
            this.time = 0;
        } else {
            this.time = (int) totalTimeOfVisibility - (int) expiredTime;
        }
    }
    @JsonIgnore
    public Restriction getRestrictionType() {
        return this.restrictionType;
    }

    @JsonIgnore
    public String getId() {
        return this.id;
    }

}
