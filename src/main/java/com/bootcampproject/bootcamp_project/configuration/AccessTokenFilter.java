package com.bootcampproject.bootcamp_project.configuration;

import com.bootcampproject.bootcamp_project.entity.User;
import com.bootcampproject.bootcamp_project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class AccessTokenFilter extends OncePerRequestFilter {

	@Autowired
	UserService userService;

	@Override
	public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
		String path = request.getRequestURI();
		if (("/oauth/token".equals(path)) || ("/login".equals(path))) {
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			List<String> errors = new ArrayList<>();
			boolean hasErrors = false;

			if (!StringUtils.hasLength(username)) {
				hasErrors = true;
				errors.add("Username cannot be blank");
			}

			if (!StringUtils.hasLength(password)) {
				hasErrors = true;
				errors.add("Password cannot be blank");
			}

			if (hasErrors) {
				response.setStatus(HttpStatus.NOT_FOUND.value());
				response.getWriter().write(String.join("\n", errors));
				return;
			}

//401 : not valid email and pwd

			User user = userService.findByEmail(username);

			if (user == null) {
				response.setStatus(HttpStatus.NOT_FOUND.value());
				response.getWriter().write("User not exists");
				return;
			}
			if (user.getIsLocked()) {
				errors.add("Your account id locked");
				hasErrors = true;
			}
			if (!user.getIsActive()) {
				errors.add("Your account id de-activated");
				hasErrors = true;
			}
			if (user.getIsExpired()) {
				errors.add("Your password id expired");
				hasErrors = true;
			}
			if (hasErrors) {
				response.setStatus(HttpStatus.LOCKED.value());
				response.getWriter().write(String.join("\n", errors));
				return;
			}
		}
		chain.doFilter(request, response);
	}
}
