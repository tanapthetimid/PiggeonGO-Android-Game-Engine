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

import java.io.Serializable;
import java.util.ArrayList;

public class Node implements Serializable
{
    private float x;
    private float y;
    private float rotation;
    private float scaleX = 1;
    private float scaleY = 1;
    private float width;
    private float height;
    private boolean instanced = false;

    //name used to identify nodes. can be useful when searching for a node
    private String nodeName = "";

    //children node
    private ArrayList<Node> children = new ArrayList<>();

    public Node()
    {
        //empty constructor
    }

    public Node(String nodeName)
    {
        this.nodeName = nodeName;
    }

    public String getName()
    {
        return nodeName;
    }

    public float getX()
    {
        return x;
    }
    public void setX(float x)
    {
        this.x = x;
    }

    public float getY()
    {
        return y;
    }
    public void setY(float y)
    {
        this.y = y;
    }

    public float getRotationAngle()
    {
        return rotation;
    }
    public void setRotationAngle(float rotation)
    {
        this.rotation = rotation;
    }

    public float getScaleX()
    {
        return scaleX;
    }
    public void setScaleX(float scaleX)
    {
        this.scaleX = scaleX;
    }

    public float getScaleY()
    {
        return scaleY;
    }
    public void setScaleY(float scaleY)
    {
        this.scaleY = scaleY;
    }

    public void setRawWidth(float width){this.width = width;}
    public void setRawHeight(float height){this.height = height;}
    public float getRawWidth(){return width;}
    public float getRawHeight(){return height;}
    public float getScaledWidth(){return width * getScaleX();}
    public float getScaledHeight(){return height * getScaleY();}

    public Node[] getChildrenAsArray()
    {
        return children.toArray(new Node[children.size()]);
    }
    public ArrayList<Node> getChildren()
    {
        return children;
    }

    public void attachChild(Node child)
    {
        children.add(child);
    }

    public void removeChild(Node child){
        children.remove(child);
    }

    public boolean isInstanced() {
        return instanced;
    }

    public void setInstanced(boolean instanced) {
        this.instanced = instanced;
    }
}