package com.kyobee.util.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.kyobee.dto.UserDTO;
import com.kyobee.util.common.Constants;



/**
 * This class is Servlet Filter implementation class AuthenticationFilter
 * 
 */
public class AuthenticationFilter implements Filter {


	
	//@Autowired
	//UserService userService;
	
	//private FilterConfig filterConfig = null;

	/**
	 * Default constructor.
	 */
	public AuthenticationFilter() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		//this.filterConfig = fConfig;
	}

	/**
	 * This method is for IDM authentication for native and web application.
	 * 
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		HttpServletRequest httpReq = (HttpServletRequest) request;
		HttpServletResponse httpRes= (HttpServletResponse) response;
		String requestURI = httpReq.getRequestURI();
		//ObjectMapper oMapper = new ObjectMapper();
		
		
		try {

			/**
			 * FOR LOGOUT - Client just has to hit this url and session would be invalidated.
			 */
			if(requestURI.contains(Constants.LOGOUT_URL)){
				this.sessionOut(httpReq);
				return;
			}
			
			
			if(requestURI.contains("kyobee/web/") && !requestURI.contains("kyobee/web/rest")){
				if (!byPassURL(requestURI)) {
					UserDTO userDTO = (UserDTO) httpReq.getSession().getAttribute(Constants.USER_OBJ);
					if (userDTO == null) {
						httpRes.sendRedirect(httpReq.getContextPath() + "/public/index.html");
					}
				}
			}
			
			
		//	log.info("************Just to test*************");
			chain.doFilter(request, response);
		
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	
	private boolean byPassURL(String url) {
		if (url.contains("/guestuuid") || url.contains("/orgseatpref") || url.contains("/usermetricks")
				|| url.contains("/pusgerinformation")) {
			System.out.println("URL Bypassed : " + url);
			return true;
		} else {
			return false;
		}
	}
	
	
	
	/**
	 * Session object
	 * 
	 * @param userObj
	 * @param httpReq
	 * @throws Exception
	 */
	/*private void sessionUserObj(UserDTO userObj, HttpServletRequest httpReq) throws Exception {
		httpReq.getSession().setAttribute(BeeyaConstants.USER_OBJ, userObj);	
	}
*/
	
	
	/**
	 * LOGOUT _ METHOD
	 * 
	 * @param httpReq
	 * @param httpRes 
	 */
	private void sessionOut(HttpServletRequest httpReq) throws Exception{
		HttpSession session = httpReq.getSession();
		if(session != null){
			Cookie[] cookies = httpReq.getCookies();
			if(cookies!= null && cookies.length >0 ){
				for(Cookie cookie : cookies){
					cookie.setMaxAge(0);// It will delete the cookie
				}
			}
			session.invalidate();
		} 		
	}
	
	

	
	
}
