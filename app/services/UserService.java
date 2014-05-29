/**
 * 
 */
package services;

import play.Application;
import securesocial.core.Identity;
import securesocial.core.IdentityId;
import securesocial.core.java.BaseUserService;
import securesocial.core.java.Token;

/**
 * @author dlasrado
 *
 */
public class UserService extends BaseUserService {

	public UserService (Application application) {
		super(application);
	}
	/* (non-Javadoc)
	 * @see securesocial.core.java.BaseUserService#doSave(securesocial.core.Identity)
	 */
	@Override
	public Identity doSave(Identity user) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see securesocial.core.java.BaseUserService#doSave(securesocial.core.java.Token)
	 */
	@Override
	public void doSave(Token token) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see securesocial.core.java.BaseUserService#doFind(securesocial.core.IdentityId)
	 */
	@Override
	public Identity doFind(IdentityId identityId) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see securesocial.core.java.BaseUserService#doFindToken(java.lang.String)
	 */
	@Override
	public Token doFindToken(String tokenId) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see securesocial.core.java.BaseUserService#doFindByEmailAndProvider(java.lang.String, java.lang.String)
	 */
	@Override
	public Identity doFindByEmailAndProvider(String email, String providerId) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see securesocial.core.java.BaseUserService#doDeleteToken(java.lang.String)
	 */
	@Override
	public void doDeleteToken(String uuid) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see securesocial.core.java.BaseUserService#doDeleteExpiredTokens()
	 */
	@Override
	public void doDeleteExpiredTokens() {
		// TODO Auto-generated method stub

	}

}
