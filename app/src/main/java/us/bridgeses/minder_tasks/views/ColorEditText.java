/*
 * Copyright 2016 Tony Bridges
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package us.bridgeses.minder_tasks.views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by tbrid on 6/14/2016.
 */
public class ColorEditText extends EditText {

    private int color;

    public ColorEditText(Context context, AttributeSet attrs) {
        this(context, attrs, Color.BLUE);
    }

    public ColorEditText(Context context, AttributeSet attrs, int color) {
        super(context, attrs);
        setColor(color);
    }

    public void setColor(int color) {
        if (color == this.color) {
            return;
        }
        getBackground().setColorFilter(color, PorterDuff.Mode.SRC_IN);
        this.color = color;
    }
}
