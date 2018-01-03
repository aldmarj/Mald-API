/**
 * 
 */
package utilities;

/**
 * Utility class to help with password validation.
 * 
 * @author Lawrence
 */
public abstract class PasswordUtils 
{
	/**
	 * Ensure the password matches the below security rules.
	 * 
	 * @param candidate - The candidate password to test.
	 * @return Whether it conforms or not.
	 */
	public static boolean conformsToSecurityRules(String candidate)
	{
		String pattern = "^"  // start-of-string
			+ "(?=.*[0-9])"   // a digit must occur at least once
			+ "(?=.*[a-z])"   // a lower case letter must occur at least once
			+ "(?=.*[A-Z])"   // an upper case letter must occur at least once
			+ "(?=\\S+$)"     // no whitespace allowed in the entire string
			+ ".{8,}"         // anything, at least eight places though
			+ "$";            // end-of-string
		
		return candidate.matches(pattern);
	}
}
