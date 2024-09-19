package spring.jsb_organic;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.HtmlUtils;

import jakarta.servlet.http.HttpSession;

@Component
public class SessionHelper 
{
    
    public void removeMessageFromSession() 
    {
        try {
            System.out.println("removing message form session ");
            HttpSession session = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest().getSession();
            session.removeAttribute("message");
               
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String HtmlUnEscaped(String htmlCode)
    {
        return HtmlUtils.htmlUnescape(htmlCode);
    }
}