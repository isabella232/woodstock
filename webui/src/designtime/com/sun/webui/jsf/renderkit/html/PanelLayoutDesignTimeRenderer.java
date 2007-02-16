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
package com.sun.webui.jsf.renderkit.html;

import com.sun.webui.jsf.component.util.DesignMessageUtil;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import com.sun.webui.jsf.component.PanelLayout;

/**
 * A delegating renderer for {@link com.sun.webui.jsf.component.PanelLayout} that
 * makes the component appear correctly at designtime when it has no children.
 *
 * @author gjmurphy
 */
public class PanelLayoutDesignTimeRenderer extends PanelDesignTimeRenderer {

    public PanelLayoutDesignTimeRenderer() {
        super(new PanelLayoutRenderer());
    }
    
    protected String getShadowText(UIComponent component) {
        return DesignMessageUtil.getMessage(PanelLayoutDesignTimeRenderer.class, "panelLayout.label");
    }

    protected String getContainerElementName(UIComponent component) {
        return "div"; //NOI18N
    }

    protected String getStyle(UIComponent component) {
        if (((PanelLayout) component).getPanelLayout().equals(PanelLayout.GRID_LAYOUT))
            return "position: relative; -rave-layout: grid";
        return null;
    }
    
}
