package br.com.primefaces.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import br.com.primefaces.model.UserPerson;


@WebFilter(urlPatterns = {"/*"})
public class LoginFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		HttpServletRequest req = (HttpServletRequest) request;
		HttpSession sessao = req.getSession();
		
		UserPerson usuario = (UserPerson) sessao.getAttribute("usuario");
		String url = req.getServletPath();
		
		if(usuario == null && !url.equalsIgnoreCase("login.jsf")) {
			RequestDispatcher dispatcher = req.getRequestDispatcher("/login.jsf");
			dispatcher.forward(request, response);
		} else {
			chain.doFilter(request, response);
		}		
	}

	@Override
	public void destroy() {
		
		
	}
	
}	
