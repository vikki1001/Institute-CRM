//
//package com.ksv.ktrccrm.security;
//
//import java.util.Collection;
//import java.util.Collections;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import com.ksv.ktrccrm.db1.entities.UserMst;
//
//public class UserDetailsImpl implements UserDetails {
//	private static final long serialVersionUID = 1L;
//
//	@Autowired
//	private UserMst user;
//
//	public UserDetailsImpl(UserMst user) {
//		super();
//		this.user = user;
//	}
//
//	@Override
//	public Collection<? extends GrantedAuthority> getAuthorities() {
//		return Collections.singleton(new SimpleGrantedAuthority(user.getMainRole()));
//	}
//
////	@Override
////	public String getPassword() {
////		return user.getPassword();
////	}
//
//	@Override
//	public String getPassword() {
//		if (user.isOTPRequired()) {
//			return user.getOneTimePassword();
//		}
//
//		return user.getPassword();
//	}
//
//	@Override
//	public String getUsername() {
//		return user.getLoginId();
//	}
//
//	@Override
//	public boolean isAccountNonExpired() {
//		return true;
//	}
//
//	@Override
//	public boolean isAccountNonLocked() {
//		return true;
//	}
//
//	@Override
//	public boolean isCredentialsNonExpired() {
//		return true;
//	}
//
//	@Override
//	public boolean isEnabled() {
//		return true;
//	}
//
//	public UserMst getUser() {
//		return this.user;
//	}
//
//	public Integer isUserLocked() {
//		return user.getIsUserLocked();
//	}
//
//	public String tenantId() {
//		return user.getTenantId();
//	}
//}



package com.ksv.ktrccrm.security;

import java.util.Collection;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.ksv.ktrccrm.db1.entities.UserMst;


public class UserDetailsImpl implements UserDetails {
	private static final long serialVersionUID = 1L;

	@Autowired
	private UserMst user;

	public UserDetailsImpl(UserMst user) {
		super();
		this.user = user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.singleton(new SimpleGrantedAuthority(user.getMainRole()));
	}

//	@Override
//	public String getPassword() {
//		return user.getPassword();
//	}

	@Override
	public String getPassword() {
//		if (user.isOTPRequired()) {
//			return user.getOneTimePassword();
//		}

		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getLoginId();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	public UserMst getUser() {
		return this.user;
	}

	public Integer isUserLocked() {
		return user.getIsUserLocked();
	}

	public String tenantId() {
		return user.getTenantId();
	}
}
