package net.johnnyconsole.cp670.project.objects;

public class Course {


    public final int crn;
    public final String title, code;
    public Course(int crn, String code, String title) {
        this.crn = crn;
        this.code = code;
        this.title = title;
    }

}
