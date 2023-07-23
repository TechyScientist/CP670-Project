package net.johnnyconsole.cp670.project.objects;

public class Student {

    public final String firstName, lastName, username;
    public String grade = "--";

    public Student(String firstName, String lastName, String username) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
    }
}
