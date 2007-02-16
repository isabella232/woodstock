/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License).  You may not use this file except in
 * compliance with the License.
 * 
 * You can obtain a copy of the license at
 * https://woodstock.dev.java.net/public/CDDLv1.0.html.
 * See the License for the specific language governing
 * permissions and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at https://woodstock.dev.java.net/public/CDDLv1.0.html.
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * you own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 */

package com.sun.webui.jsf.component;

import com.sun.rave.designtime.DesignBean;
import com.sun.rave.designtime.DesignContext;
import com.sun.rave.designtime.DesignInfo;
import com.sun.rave.designtime.DesignProperty;
import com.sun.rave.designtime.DisplayAction;
import com.sun.rave.designtime.Result;
import com.sun.rave.designtime.ResultMessage;
import com.sun.rave.designtime.impl.BasicDisplayAction;
import com.sun.webui.jsf.design.AbstractDesignInfo;
import com.sun.webui.jsf.util.Bundle;

import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;

/**
 * Design time behavior for a <code>Label</code> component. The
 * following behavior is provided:</p>
 * <ul>
 * <li>When component is created, default the <code>text</code> property to the
 *     component's display name, and the <code>labelLevel</code> to "3".</li>
 * <li>Allow the component to be linked to other components that implement
 *     <code>EditableValueHolder</code>. When linked, the label's
 *     <code>for</code> property will be set to the <code>id</code> of the
 *     target component. If the target component has a lable property already,
 *     a display action is returned that asks the user if the label should
 *     be transfered. If the user chooses to transfer the label, the label
 *     and labelLevel property values are moved.</li>
 * <li>If the component's <code>for</code> property is modified,
 *     <code>requiredIndicator</code> property is reset to reflect the value of
 *     the linked input component's <code>requried</code> property.</li>
 * </ul>
 */

public class LabelDesignInfo extends AbstractDesignInfo {

    /**
     * Construct a new <code>LabelDesignInfo</code> instance.
     */
    public LabelDesignInfo() {
        super(Label.class);
    }

    /**
     * @param bean for the newly created instance
     */
    public Result beanCreatedSetup(DesignBean bean) {
        DesignProperty textProperty = bean.getProperty("text"); //NOI18N
        textProperty.setValue(bean.getBeanInfo().getBeanDescriptor().getDisplayName());
        return Result.SUCCESS;
    }


    /**
     * Returns true if source bean implements EditaleValueHolder.
     *
     * @param targetBean Target <code>Label</code> bean
     * @param sourceBean Source bean (or <code>null</code>)
     * @param sourceClass Class of source object being dropped
     */
    public boolean acceptLink(DesignBean targetBean, DesignBean sourceBean,
                                Class sourceClass) {
        if (EditableValueHolder.class.isAssignableFrom(sourceClass))
            return true;
        return super.acceptLink(targetBean, sourceBean, sourceClass);
    }
    
    /**
     * Returns true if child is an instance of UIParameter.
     */
    public boolean acceptChild(DesignBean parentBean, DesignBean childBean, Class childClass) {
        if (UIParameter.class.isAssignableFrom(childClass))
            return true;
        return super.acceptChild(parentBean, childBean, childClass);
    }

    /** 
     * If a component that implements <code>EditableValueHolder</code> is
     * linked to us, update our <code>for</code> property such that it contains
     * the component's id. Links from all other types of components are treated
     * as no-ops.
     *
     * @param targetBean Target <code>Label</code> bean
     * @param sourceBean Source bean (or <code>null</code>)
     */
    public Result linkBeans(DesignBean targetBean, DesignBean sourceBean) {

        if (!EditableValueHolder.class.isAssignableFrom(sourceBean.getInstance().getClass()))
            return super.linkBeans(targetBean, sourceBean);

        DesignProperty forProperty = targetBean.getProperty("for"); //NOI18N
        if (forProperty == null)
            return Result.FAILURE;
        forProperty.setValue(sourceBean.getInstanceName());
        return Result.SUCCESS;
    }

}
