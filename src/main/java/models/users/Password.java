package models.users;

import org.apache.catalina.CredentialHandler;
import org.apache.catalina.realm.SecretKeyCredentialHandler;

import java.security.NoSuchAlgorithmException;
import java.util.ResourceBundle;

/**
 * Class representing a users stored password. i.e. after the plain text has been hashed.
 * this allows central setting of algorithm and easier comparisons to other passwords.
 */
public class Password
{
    private static CredentialHandler ch = null;

    private final String hashedValue;

    /**
     * checks the credential handler has been created, if it has not then it creates it.
     *
     * @return the credential handler
     * @throws NoSuchAlgorithmException if the credential handler algorithm is not supported
     */
    private static CredentialHandler checkCredentialHandler() throws NoSuchAlgorithmException
    {
        if (ch == null)
        {
            final ResourceBundle rb = ResourceBundle.getBundle("auth"); //NON-NLS
            final SecretKeyCredentialHandler skch = new SecretKeyCredentialHandler();
            skch.setAlgorithm(rb.getString("auth.password.algorithm")); //NON-NLS
            skch.setKeyLength(Integer.parseInt(rb.getString("auth.password.keyLength"))); //NON-NLS
            ch = skch;
        }
        return ch;
    }

    /**
     * Generates the equivalent stored credentials for the given input credentials.
     *
     * @param input User provided credentials
     * @return The equivalent stored credentials for the given input credentials
     * @throws NoSuchAlgorithmException if the credential handler algorithm is not supported
     */
    public static String mutate(final String input) throws NoSuchAlgorithmException
    {
        checkCredentialHandler();
        return ch.mutate(input);
    }

    /**
     * creates a new Password by first mutating the input plain text.
     *
     * @param text - the plain text to change into the equivalent stored credentials.
     * @return the password representing the input plain text.
     * @throws NoSuchAlgorithmException if the credential handler algorithm is not supported
     */
    public static Password fromPlainText(final String text) throws NoSuchAlgorithmException
    {
        return Password.fromHash(Password.mutate(text));
    }

    /**
     * creates a new Password from an already Hashed string. e.g. from the database.
     *
     * @param hashedText - the already hashed string
     * @return the digest representing the input text.
     */
    public static Password fromHash(final String hashedText)
    {
        return new Password(hashedText);
    }

    /**
     * private class constructor
     *
     * @param hashedText the already hashed string
     */
    private Password(final String hashedText)
    {
        this.hashedValue = hashedText;
    }

    /**
     * checks to see if the plainText matches this Password.
     *
     * @param plainText - the text to compare to.
     * @return {@code true} if the plainText matches the hashed password
     * @throws NoSuchAlgorithmException if the credential handler algorithm is not supported
     */
    public boolean matches(final String plainText) throws NoSuchAlgorithmException
    {
        checkCredentialHandler();
        return ch.matches(plainText, this.hashedValue);
    }

    @Override
    public boolean equals(final Object obj)
    {
        return (obj instanceof Password) && ((Password) obj).hashedValue.equals(this.hashedValue);
    }

    @Override
    public String toString()
    {
        return this.hashedValue;
    }
}
