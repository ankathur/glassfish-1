/*
 * Copyright (c) 1997, 2018 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

package wftest2;

import java.io.IOException;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

@WebFilter(urlPatterns={ "/" }, dispatcherTypes= { DispatcherType.REQUEST })
public class WFTestFilter2B implements Filter {
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println(">>> WFTestFilter2B.init");
    }   

    public void doFilter(ServletRequest req, ServletResponse res,
            FilterChain chain) throws IOException, ServletException {

        System.out.println(">>> WFTestFilter2B.doFilter");
        String filterMessage = (String)req.getAttribute("filterMessage");
        if (filterMessage == null) {
            filterMessage = "";
        }
        filterMessage += "B";

        req.setAttribute("filterMessage", filterMessage);
        chain.doFilter(req, res);
    }

    public void destroy() {
        System.out.println(">>> WFTestFilter2B.destroy");
    }
}
