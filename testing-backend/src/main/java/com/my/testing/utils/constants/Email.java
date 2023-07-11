package com.my.testing.utils.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Email {
    /** For new users only */
    public static final String SUBJECT_GREETINGS = "Welcome to TestHub!";

    /** Any notification letter subject */
    public static final String SUBJECT_NOTIFICATION = "TestHub notification!";

    /** Place user's name instead of %s */
    public static final String HELLO = "Hello, %s,<br>";
    public static final String INFORMATION = "We have some important information for you:";
    public static final String FOOTER = "Best wishes,<br>TestHub team";
    public static final String DOUBLE_ENTER = "<br><br>";


    /** Greetings for new users */
    public static final String MESSAGE_GREETINGS = HELLO +
            "Are you ready for a new brainstorm?<br>" +
            "Then you are in the right place! Just <a href=\"http://localhost:5173/sign_in\">sign in</a>" +
            " and choose the suitable test for you. Our team develops new tests on different themes. " +
            "Difficulty, number of questions and time for solving in test can differ. A question can have one or more correct answers. " +
            "The result of the test is the percentage of questions that the student answered correctly in relation to the total number of questions.<br>" +
            "Thank you for choosing TestHub!" +
            DOUBLE_ENTER +
            FOOTER;

    /** Place token instead of %s */
    public static final String MESSAGE_RESET_PASSWORD = HELLO +
            INFORMATION +
            DOUBLE_ENTER +
            "You have requested to reset your password.<br>" +
            "Click the link below to change your password:<br>" +
            "<a href=\"http://localhost:5173/reset_pass?token=%s\">Change my password</a>" +
            DOUBLE_ENTER +
            "<b>Ignore this email if you do remember your password, or you have not made the request.</b>" +
            DOUBLE_ENTER +
            FOOTER;
}
