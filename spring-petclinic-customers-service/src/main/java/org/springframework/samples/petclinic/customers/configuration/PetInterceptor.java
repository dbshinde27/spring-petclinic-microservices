package org.springframework.samples.petclinic.customers.configuration;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class PetInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// log.info("Inside the Pre Handle method");
		log.info("===========================Request begin======================================");
		log.info("[Inside the Pre Handle method][" + request + "]" + "[" + request.getMethod() + "]"
				+ request.getRequestURI() + getParameters(request));
		log.info("URI         : {}", request.getServletPath());
		log.info("Method      : {}", request.getMethod());
		log.info("Headers     : {}", request.getHeaderNames());
		log.info("==========================Request end=========================================");

		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		log.info("Inside the Post Handle method");
		log.info("============================Response begin====================================");
		log.info("Response         : {}", response.toString());
		log.info("Status code  : {}", response.getStatus());
		log.info("Headers      : {}", response.getHeaderNames());
		log.info("=======================Response end===========================================");

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception exception) throws Exception {
		log.info("After completion of request and response");
	}

	private String getParameters(HttpServletRequest request) {
		StringBuffer posted = new StringBuffer();
		Map<String, String[]> e = request.getParameterMap();
		if (e != null) {
			posted.append("?");
		}
		for (String curr : e.keySet()) {
			if (posted.length() > 1) {
				posted.append("&");
			}

			posted.append(curr + "=");
			if (curr.contains("password") || curr.contains("pass") || curr.contains("pwd")) {
				posted.append("*****");
			}
			else {
				posted.append(request.getParameter(curr));
			}
		}
		String ip = request.getHeader("X-FORWARDED-FOR");
		String ipAddr = (ip == null) ? getRemoteAddr(request) : ip;
		if (ipAddr != null && !ipAddr.equals("")) {
			posted.append("&_psip=" + ipAddr);
		}
		return posted.toString();
	}

	private String getRemoteAddr(HttpServletRequest request) {
		String ipFromHeader = request.getHeader("X-FORWARDED-FOR");
		if (ipFromHeader != null && ipFromHeader.length() > 0) {
			log.debug("ip from proxy - X-FORWARDED-FOR : " + ipFromHeader);
			return ipFromHeader;
		}
		return request.getRemoteAddr();
	}

}
