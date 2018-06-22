/* 
 * Copyright (c) 2017, Tanapoom Sermchaiwong
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are 
 * met:
 * 
 * * Redistributions of source code must retain the above copyright 
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name 'Piggeon' nor the names of 
 *   its contributors may be used to endorse or promote products derived 
 *   from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR 
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, 
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, 
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR 
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING 
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package piggeongo.piggeon;

import android.graphics.Bitmap;
import android.os.Build;
import android.util.Log;

import piggeongo.util.ImageInfo;
import piggeongo.util.PiggeonImageUtils;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class GameObject extends Node implements Serializable, Updatable
{
    private int objectID = 0;
    private ImageInfo imageInfo;
    private ArrayList<Updatable> goUpdatables = new ArrayList<>();
    private Animator animator = null;

    public void setObjectID(int objectID){
        this.objectID = objectID;
    }

    public int getObjectID(){
        return objectID;
    }

    protected ArrayList<Updatable> getGoUpdatables() {
        return goUpdatables;
    }

    /**
     * load method that should be called in onLoad() in the Stage
     */
    public void load()
    {
        onLoad();
    }

    //onload callback used to load object (like textures or other temporary values)
    public abstract void onLoad();

    /**
     * update interface method called on loop.
     *
     * this method updates all the Updatable(s) that the GameObject holds
     * and also updates the GameObject(s) bounded Animator.
     *
     * finally, onUpdate callback is called  to child
     */
    public void update(Stage stage)
    {
        onUpdate(stage);

        Object[] goUpArray = goUpdatables.toArray();
        for(Object updatable: goUpArray){
            ((Updatable)updatable).update(stage);
        }

        if(animator != null)
        {
            animator.update(stage);
        }
    }

    //onUpdate callback where most game related work is done
    //must be overridden by child
    public abstract void onUpdate(Stage stage);

    /**
     * animator complete callback will be called when the Animator finishes
     * one iteration of the animation. even in looping mode, this callback will be called
     */
    public void onAnimationComplete(String animatorID){}
    
    //-----------------------interface methods

    //modify updatables
    public void addUpdatable(Updatable updatable)
    {
        goUpdatables.add(updatable);
    }
    public void removeUpdatable(Updatable updatable)
    {
        goUpdatables.remove(updatable);
    }

    //binds an Animator to this GameObject
    public void setAnimator(Animator animator) {
        this.animator = animator;
        animator.setTargetGameObject(this);
    }

    /**
     * Set the texture to the texture specified in path.
     * No need to worry about reloading textures, since ImageUtils
     * cache textureID, so there is no performance hit when switching
     * between textures.
     */
    public void setTexture(int resourceId)
    {
        imageInfo = PiggeonImageUtils.loadImage(resourceId);
        setRawHeight(imageInfo.height);
        setRawWidth(imageInfo.width);
    }

    public void setTexture(Bitmap bitmap, int bitmapID)
    {
        imageInfo = PiggeonImageUtils.loadImageFromBitmap(bitmap, bitmapID);
        setRawHeight(imageInfo.height);
        setRawWidth(imageInfo.width);
    }

    public void setTexture(Bitmap bitmap)
    {
        imageInfo = PiggeonImageUtils.loadImageFromBitmapNoCache(bitmap);
        setRawHeight(imageInfo.height);
        setRawWidth(imageInfo.width);
    }

    public void setSquareNoTexture(float width, float height, float[] color){
        imageInfo = PiggeonImageUtils.createSquareImage(width, height, color);
        setRawHeight(imageInfo.height);
        setRawWidth(imageInfo.width);
    }

    public void setImageInfo(ImageInfo info)
    {
        this.imageInfo = info;
    }

    //accessors
    public Animator getAnimator() {
        return animator;
    }
    public ImageInfo getImageInfo(){return imageInfo;}
}