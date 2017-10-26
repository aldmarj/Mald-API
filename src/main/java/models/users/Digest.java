package models.users;

import org.apache.catalina.CredentialHandler;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class Digest
{
    private static final String CREDENTIAL_HANDLER_NAME = "java:/comp/env/passwordHandler"; //NON-NLS
    private static CredentialHandler ch = null;

    private final String value;

    static
    {
        final Context init;
        try
        {
            init = new InitialContext();
            ch = (CredentialHandler) init.lookup(Digest.CREDENTIAL_HANDLER_NAME);
        }
        catch (final NamingException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * creates a new Digest by first mutating the input plain text.
     *
     * @param text - the plain text to change into the equivalent stored credentials.
     * @return the digest representing the input plain text.
     */
    public static Digest fromPlainText(final String text)
    {
        return fromDigested(ch.mutate(text));
    }

    public static Digest fromDigested(final String digested)
    {
        return new Digest(digested);
    }

    private Digest(final String digestedString)
    {
        value = digestedString;
    }

    public boolean matches(final String inputCredentials)
    {
        return ch.matches(inputCredentials, this.value);
    }

    @Override
    public boolean equals(final Object obj)
    {
        if (obj instanceof Digest)
        {
            return ((Digest) obj).value.equals(this.value);
        }
        return false;
    }

    @Override
    public String toString()
    {
        return this.value;
    }
}
