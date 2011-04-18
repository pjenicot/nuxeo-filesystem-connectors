package org.nuxeo.ecm.platform.wi.filter;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.platform.web.common.requestcontroller.filter.NuxeoRequestControllerFilter;
import org.nuxeo.ecm.platform.web.common.requestcontroller.filter.RemoteHostGuessExtractor;
import org.nuxeo.runtime.transaction.TransactionHelper;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.Principal;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Organization: Gagnavarslan ehf
 */
public class WIRequestFilter implements Filter {

    public static String WEBDAV_USERAGENT = "Microsoft-WebDAV-MiniRedir";
    public static String MSOFFICE_USERAGENT = "Microsoft Office Existence Discovery";
    public static final String SESSION_KEY = "org.nuxeo.ecm.platform.wi.session";
    public static final String BACKEND_KEY = "org.nuxeo.ecm.platform.wi.backend";
    public static final String SESSION_LOCK_KEY = "SessionLockKey";
    public static final String SESSION_LOCK_TIME = "SessionLockTime";
    public static final String SYNCED_REQUEST_FLAG = "NuxeoSessionAlreadySync";
    public static final int LOCK_TIMOUT_S = 120;
    private static final Log log = LogFactory.getLog(WIRequestFilter.class);

    public void init(FilterConfig filterConfig) throws ServletException {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        if (isWIRequest(httpRequest)) {
            WISession session = SessionCacheHolder.getInstance().getCache().get(httpRequest);
            httpRequest.setAttribute(SESSION_KEY, session);


            boolean sessionSynched = false;
            sessionSynched = simpleSyncOnSession(httpRequest);
            boolean txStarted = false;
            try {
                txStarted = TransactionHelper.startTransaction();
                if (txStarted) {
                    response = new NuxeoRequestControllerFilter.CommittingServletResponseWrapper(httpResponse);
                }
                chain.doFilter(request, response);
            } catch (Exception e) {
                log.error(doFormatLogMessage(httpRequest, "Unhandled error was cauth by the Filter"), e);
                if (txStarted) {
                    if (log.isDebugEnabled()) {
                        log.debug(doFormatLogMessage(httpRequest, "Marking transaction for RollBack"));
                    }
                    try {
                        TransactionHelper.setTransactionRollbackOnly();
                    } catch (Exception e1) {
                        log.warn("Could not mark transaction as rollback only.");
                    }
                }
                throw new ServletException(e);
            } finally {
                if (txStarted) {
                    if (!((NuxeoRequestControllerFilter.CommittingServletResponseWrapper) response).committedTx) {
                        TransactionHelper.commitOrRollbackTransaction();
                    }
                }
                if (sessionSynched) {
                    simpleReleaseSyncOnSession(httpRequest);
                }
                if (log.isDebugEnabled()) {
                    log.debug(doFormatLogMessage(httpRequest, "Exiting NuxeoRequestControler filter"));
                }
            }
        } else {
            chain.doFilter(request, response);
            return;
        }
    }

    public void destroy() {
    }

    private boolean isWIRequest(HttpServletRequest request) {
        String ua = request.getHeader("User-Agent");
        return StringUtils.isNotEmpty(ua) && (ua.contains(WEBDAV_USERAGENT) || ua.contains(MSOFFICE_USERAGENT));
    }

    protected boolean simpleSyncOnSession(HttpServletRequest request) {
        if (log.isDebugEnabled()) {
            log.debug(doFormatLogMessage(request, "Trying to sync on session "));
        }

        if (request.getAttribute(SYNCED_REQUEST_FLAG) != null) {
            if (log.isWarnEnabled()) {
                log.warn(doFormatLogMessage(request,
                        "Request has already be synced, filter is reentrant, exiting without locking"));
            }
            return false;
        }

        WISession session = (WISession) request.getAttribute(SESSION_KEY);

        Lock lock = (Lock) session.getAttribute(SESSION_LOCK_KEY);
        if (lock == null) {
            lock = new ReentrantLock();
            session.setAttribute(SESSION_LOCK_KEY, lock);
        }

        boolean locked = false;
        try {
            locked = lock.tryLock(LOCK_TIMOUT_S, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            log.error(doFormatLogMessage(request, "Unable to acuire lock for Session sync"), e);
            return false;
        }

        if (locked) {
            request.setAttribute(SYNCED_REQUEST_FLAG, true);
            request.setAttribute(SESSION_LOCK_TIME, System.currentTimeMillis());
            if (log.isDebugEnabled()) {
                log.debug(doFormatLogMessage(request,
                        "Request synced on session"));
            }
        } else {
            if (log.isDebugEnabled()) {
                log.debug(doFormatLogMessage(request, "Sync timeout"));
            }
        }

        return locked;
    }

    protected void simpleReleaseSyncOnSession(HttpServletRequest request) {
        /*HttpSession httpSession = request.getSession(false);
        if (httpSession == null) {
            if (log.isDebugEnabled()) {
                log.debug(doFormatLogMessage(
                        request,
                        "No more HttpSession : can not unlock !, HttpSession must have been invalidated"));
            }
            return;
        }*/

        WISession session = (WISession) request.getAttribute(SESSION_KEY);

        log.debug("Trying to unlock on httpSession key " + session.getKey() + " WISession:" + session.getKey()
                + " on Thread " + Thread.currentThread().getId());

        Lock lock = (Lock) session.getAttribute(SESSION_LOCK_KEY);
        if (lock == null) {
            log.error("Unable to find session lock, HttpSession may have been invalidated");
        } else {
            lock.unlock();
            if (log.isDebugEnabled()) {
                log.debug("session unlocked on Thread ");
                log.debug(doExecutionRequestLogMessage(request));
            }
        }
    }

    protected String doFormatLogMessage(HttpServletRequest request, String info) {
        String remoteHost = RemoteHostGuessExtractor.getRemoteHost(request);
        Principal principal = request.getUserPrincipal();
        String principalName = principal != null ? principal.getName() : "none";
        String uri = request.getRequestURI();
        String method = request.getMethod();
        HttpSession session = request.getSession(false);
        String sessionId = session != null ? session.getId() : "none";
        String threadName = Thread.currentThread().getName();
        return "remote=" + remoteHost + ",principal=" + principalName
                + ",uri=" + uri + ", method=" + method + ",session=" + sessionId + ",thread=" + threadName + ",info=" + info;
    }

    protected String doExecutionRequestLogMessage(HttpServletRequest request){
        Object lockTime = request.getAttribute(SESSION_LOCK_TIME);
        if(lockTime != null){
            long executionTime = System.currentTimeMillis() - (Long)lockTime;
            return doFormatLogMessage(request, "Execution time:" + executionTime + " ms.");
        } else {
            return doFormatLogMessage(request, "Unknown time of execution");
        }
    }

}
