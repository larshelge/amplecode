package org.amplecode.cave.process.state;

/*
 * Copyright (c) 2008, the original author or authors.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the AmpleCode project nor the names of its
 *       contributors may be used to endorse or promote products derived from
 *       this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

import javax.swing.BoundedRangeModel;
import javax.swing.DefaultBoundedRangeModel;
import javax.swing.event.ChangeListener;

import org.amplecode.cave.process.ProcessExecutor;

/**
 * {@link ProcessExecutor.State} class for processes which wish to report
 * progress by messages and numbers. A {@link DefaultBoundedRangeModel} is used
 * internally, which thus provides the default values. See the respective
 * interfaces and classes for documentation.
 * 
 * @see BoundedRangeModel
 * @see DefaultBoundedRangeModel
 * @see ProcessExecutor.State
 * @see MessageState
 * 
 * @author Torgeir Lorange Ostby
 * @version $Id: BoundedRangeModelState.java 73 2008-02-04 18:16:30Z torgeilo $
 */
public class BoundedRangeModelState
    extends MessageState
    implements BoundedRangeModel
{
    private DefaultBoundedRangeModel model = new DefaultBoundedRangeModel();

    // -------------------------------------------------------------------------
    // BoundedRangeModel redirects
    // -------------------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.BoundedRangeModel#addChangeListener(javax.swing.event.ChangeListener)
     */
    public final void addChangeListener( ChangeListener x )
    {
        model.addChangeListener( x );
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.BoundedRangeModel#getExtent()
     */
    public final int getExtent()
    {
        return model.getExtent();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.BoundedRangeModel#getMaximum()
     */
    public final int getMaximum()
    {
        return model.getMaximum();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.BoundedRangeModel#getMinimum()
     */
    public final int getMinimum()
    {
        return model.getMinimum();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.BoundedRangeModel#getValue()
     */
    public final int getValue()
    {
        return model.getValue();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.BoundedRangeModel#getValueIsAdjusting()
     */
    public final boolean getValueIsAdjusting()
    {
        return model.getValueIsAdjusting();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.BoundedRangeModel#removeChangeListener(javax.swing.event.ChangeListener)
     */
    public final void removeChangeListener( ChangeListener x )
    {
        model.removeChangeListener( x );
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.BoundedRangeModel#setExtent(int)
     */
    public final void setExtent( int newExtent )
    {
        model.setExtent( newExtent );
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.BoundedRangeModel#setMaximum(int)
     */
    public final void setMaximum( int newMaximum )
    {
        model.setMaximum( newMaximum );
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.BoundedRangeModel#setMinimum(int)
     */
    public final void setMinimum( int newMinimum )
    {
        model.setMinimum( newMinimum );
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.BoundedRangeModel#setRangeProperties(int, int, int, int,
     *      boolean)
     */
    public final void setRangeProperties( int value, int extent, int min, int max, boolean adjusting )
    {
        model.setRangeProperties( value, extent, min, max, adjusting );
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.BoundedRangeModel#setValue(int)
     */
    public final void setValue( int newValue )
    {
        model.setValue( newValue );
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.BoundedRangeModel#setValueIsAdjusting(boolean)
     */
    public final void setValueIsAdjusting( boolean b )
    {
        model.setValueIsAdjusting( b );
    }

    // -------------------------------------------------------------------------
    // Convenience methods
    // -------------------------------------------------------------------------

    /**
     * Returns the value relative to the range. For example: If value ==
     * minimum, 0.0f is returned. If value == maximum, 1.0f is returned. Useful
     * for displaying percentages.
     */
    public final float getRelativeValue()
    {
        return Math.abs( (model.getValue() - model.getMinimum()) / (model.getMaximum() - model.getMinimum()) );
    }

    /**
     * Increments the value by one.
     */
    public final void incrementValue()
    {
        model.setValue( model.getValue() + 1 );
    }

    /**
     * Sets the value to minimum.
     */
    public final void setValueToMinimum()
    {
        model.setValue( model.getMinimum() );
    }

    /**
     * Sets the value to maximum.
     */
    public final void setValueToMaximum()
    {
        model.setValue( model.getMaximum() );
    }
}
