package jp.co.axa.apidemo.security.services;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import jp.co.axa.apidemo.entities.User;
import jp.co.axa.apidemo.repositories.UserRepository;

@Component
public class UserDetailsServiceImpl implements UserDetailsService{
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		Optional<User> userOpt = userRepository.findByUsername(username);
		
		User user = userOpt.get();
		Collection<GrantedAuthority> authorities = new HashSet();
		
		return new UserDetailsImpl(user.getId(), user.getUsername(), user.getPassword(), authorities);
//		return new UserDetailsImpl(1L, "admin", "admin", authorities);
	}
	
	

}
