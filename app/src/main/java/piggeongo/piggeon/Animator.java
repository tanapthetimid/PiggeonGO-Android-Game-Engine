/*
 * Copyright (c) 2017, Tanapoom Sermchaiwong
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * - Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * - Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * - Neither the name Piggeon nor the names of its contributors may be
 *   used to endorse or promote products derived from this software
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package piggeongo.piggeon;

import piggeongo.util.ImageInfo;
import piggeongo.util.PiggeonImageUtils;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Poom on 11/19/2017.
 */
public class Animator implements Updatable, Serializable
{
    private GameObject targetGameObject;

    //a table of sprite and their duration
    private ArrayList<Object[]> spriteTimeTable = new ArrayList<>();

    //variables used for tracking current frame and sprite
    private int currentSpriteIndex = 0;
    private int currentFrameIndex = 0;

    //id of this animator
    private String id;

    //whether to loop the sprite or not
    private boolean looping = false;

    //to query whether the animation is complete
    private boolean completed = false;

    public Animator(String id)
    {
        this.id = id;
    }

    //this load method can be overidden by child to load preset animation
    public void load(){};

    //updates the targetGameObject's texture according to frame number and sprite index
    public void update(Stage stage)
    {
        if(targetGameObject != null && !spriteTimeTable.isEmpty() && currentSpriteIndex < spriteTimeTable.size())
        {
            if(currentFrameIndex == 0)
            {
                targetGameObject.setImageInfo((ImageInfo) spriteTimeTable.get(currentSpriteIndex)[0]);
            }
            currentFrameIndex++;

            if(currentFrameIndex >= (Integer) spriteTimeTable.get(currentSpriteIndex)[1])
            {
                 currentSpriteIndex++;
                 currentFrameIndex = 0;
            }

            if(currentSpriteIndex >= spriteTimeTable.size())
            {
                if(looping)
                {
                    currentSpriteIndex = 0;
                }

                targetGameObject.onAnimationComplete(this.id);
            }
        }

        if(currentSpriteIndex >= spriteTimeTable.size() && !looping){
            completed = true;
        }
    }

    public void addSpriteToMap(ImageInfo imageInfo, int durationInFrames)
    {
        spriteTimeTable.add(new Object[]{imageInfo, durationInFrames});
    }

    public void addSpriteToMap(int resourceId, int durationInFrames)
    {
        ImageInfo imageInfo = PiggeonImageUtils.loadImage(resourceId);
        spriteTimeTable.add(new Object[]{imageInfo, durationInFrames});
    }

    public void clearSpriteMap()
    {
        spriteTimeTable = new ArrayList<>();
    }

    public GameObject getTargetGameObject() {
        return targetGameObject;
    }

    public void setTargetGameObject(GameObject targetGameObject) {
        this.targetGameObject = targetGameObject;
    }

    public boolean isLooping() {
        return looping;
    }

    public void setLooping(boolean loop) {
        this.looping = loop;
    }

    public boolean isCompleted(){
        return completed;
    }

    public int getCurrentSpriteIndex(){
        return currentSpriteIndex;
    }
}
