/*******************************************************************************
 * Copyright (c) 2019 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-2.0/
 * 
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package mpGraphQL10.metrics;

import java.util.List;

public class AllWidgetData {

    private List<Widget> allWidgets;

    public List<Widget> getAllWidgets() {
        return allWidgets;
    }

    public void setGetCountWidget(List<Widget> allWidgets) {
        this.allWidgets = allWidgets;
    }

    public void setGetTimeWidget(List<Widget> allWidgets) {
        this.allWidgets = allWidgets;
    }
}
