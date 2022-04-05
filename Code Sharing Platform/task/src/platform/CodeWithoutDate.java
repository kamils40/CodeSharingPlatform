package platform;


public class CodeWithoutDate {

        private String code;
        private int views;
        private int time;
        public CodeWithoutDate() {}

        public CodeWithoutDate(String s,int views, int time) {

            this.code = s;
            this.views = views;
            this.time = time;
        }

        public String getCode() {
            return this.code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public void setTime(int time) {
            this.time = time;
        }
        public int getTime() {
            return this.time;
        }
        public void setViews(int views) {
            this.views = views;
        }
        public int getViews() {
            return this.views;
        }


}

