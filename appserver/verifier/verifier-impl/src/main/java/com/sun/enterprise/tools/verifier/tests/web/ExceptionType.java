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

package com.sun.enterprise.tools.verifier.tests.web;

import java.util.*;
import com.sun.enterprise.deployment.*;
import com.sun.enterprise.tools.verifier.*;
import com.sun.enterprise.tools.verifier.tests.*;
import org.glassfish.web.deployment.descriptor.ErrorPageDescriptor;
import org.glassfish.web.deployment.descriptor.WebBundleDescriptorImpl;


/** 
 * Exception-type element contains a fully qualified class name of a Java 
 * exception type.
 */
public class ExceptionType extends WebTest implements WebCheck { 

    
    /** 
     * Exception-type element contains a fully qualified class name of a Java 
     * exception type.
     *
     * @param descriptor the Web deployment descriptor
     *   
     * @return <code>Result</code> the results for this assertion
     */
    public Result check(WebBundleDescriptor descriptor) {

	Result result = loadWarFile(descriptor);
	ComponentNameConstructor compName = getVerifierContext().getComponentNameConstructor();

	if (((WebBundleDescriptorImpl)descriptor).getErrorPageDescriptors().hasMoreElements()) {
	    boolean oneFailed = false;
	    int oneExceptionType = 0;
	    int oneNA = 0;
	    boolean foundIt = false;
	    // get the errorpage's in this .war
	    for (Enumeration e = ((WebBundleDescriptorImpl)descriptor).getErrorPageDescriptors() ; e.hasMoreElements() ;) {
		foundIt = false;
                oneExceptionType++;
		ErrorPageDescriptor errorpage = (ErrorPageDescriptor) e.nextElement();
                if (errorpage.getErrorCode() == 0) {
		    String exceptionType = errorpage.getExceptionType();
		    if ((exceptionType != null) && (exceptionType.length() > 0)) {
		        boolean isValidExceptionType = false;
			try {
			    Class c = loadClass(result, exceptionType);
			    if (isSubclassOf(c, "java.lang.Exception")) {
			      isValidExceptionType = true;
			    }
			} catch (Exception ex) {
			  // should already be set
			  isValidExceptionType = false;
			}
			
			if (isValidExceptionType) {
			    foundIt = true;
			} else {
			    foundIt = false;
			}
   
			if (foundIt) {
			    result.addGoodDetails(smh.getLocalString
					   ("tests.componentNameConstructor",
					    "For [ {0} ]",
					    new Object[] {compName.toString()}));

			    result.addGoodDetails(smh.getLocalString
						  (getClass().getName() + ".passed",
						   "Exception type [ {0} ] contains a fully qualified class name of a Java exception type within web application [ {1} ]",
						   new Object[] {exceptionType, descriptor.getName()}));
			} else {
			    if (!oneFailed) {
				oneFailed = true;
			    }
			    result.addErrorDetails(smh.getLocalString
					   ("tests.componentNameConstructor",
					    "For [ {0} ]",
					    new Object[] {compName.toString()}));

			    result.addErrorDetails(smh.getLocalString
						   (getClass().getName() + ".failed",
						    "Error: Exception type [ {0} ] does not contain a fully qualified class name of a Java exception type within web application [ {1} ]",
						    new Object[] {exceptionType, descriptor.getName()}));
			}
		    } else {
			if (!oneFailed) {
			    oneFailed = true;
			}
			Integer errorCode = new Integer( errorpage.getErrorCode() );
			result.addErrorDetails(smh.getLocalString
					   ("tests.componentNameConstructor",
					    "For [ {0} ]",
					    new Object[] {compName.toString()}));

			result.addErrorDetails(smh.getLocalString
					       (getClass().getName() + ".failed",
						"Error: Exception type [ {0} ] does not contain a fully qualified class name of a Java exception type within web application [ {1} ]",
						new Object[] {errorCode.toString(), descriptor.getName()}));
		        oneNA++;
		    }  
		} else {
		    // maybe Exception is null 'cause we are using ErrorCode
		    // if that is the case, then test is N/A, 
		    Integer errorCode = new Integer( errorpage.getErrorCode() );
		    result.addNaDetails(smh.getLocalString
					   ("tests.componentNameConstructor",
					    "For [ {0} ]",
					    new Object[] {compName.toString()}));

		    result.addNaDetails(smh.getLocalString
					(getClass().getName() + ".notApplicable1",
					 "Exception type is null, using error-code [ {0} ] instead within web application [ {1} ]",
					 new Object[] {errorCode.toString(), descriptor.getName()}));
		    oneNA++;
		}  
	    }
	    if (oneFailed) {
		result.setStatus(Result.FAILED);
	    } else if (oneNA == oneExceptionType) {
		result.setStatus(Result.NOT_APPLICABLE);
	    } else {
		result.setStatus(Result.PASSED);
	    }
	} else {
	    result.addNaDetails(smh.getLocalString
					   ("tests.componentNameConstructor",
					    "For [ {0} ]",
					    new Object[] {compName.toString()}));

	    result.notApplicable(smh.getLocalString
				 (getClass().getName() + ".notApplicable",
				  "There are no exception-type elements within the web archive [ {0} ]",
				  new Object[] {descriptor.getName()}));
	}

	return result;
    }
}
