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

import java.io.*;
import java.util.HashMap;

public class SaveState implements Serializable
{
    private HashMap<Integer, Stage> stages;

    public SaveState()
    {
        stages = new HashMap<>();
    }

    public boolean isEmpty()
    {
        return stages.isEmpty();
    }

    // add/remove stage
    public void saveStage(int index, Stage stage)
    {
        stages.put(index, (Stage) cloneSerializable(stage));
    }
    public Stage getStage(int index)
    {
        return stages.get(index);
    }

    public Serializable cloneSerializable(Serializable serializable)
    {
        try {
            ByteArrayOutputStream bOut = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bOut);
            out.writeObject(serializable);
            out.close();

            byte[] payload = bOut.toByteArray();

            ByteArrayInputStream bIn = new ByteArrayInputStream(payload);
            ObjectInputStream in = new ObjectInputStream(bIn);
            Serializable clone = (Serializable) in.readObject();
            in.close();

            return clone;
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public void saveToFile(String filename)
    {
        FileOutputStream fileOutputStream = null;
        ObjectOutputStream objectOutputStream = null;
        try
        {
            fileOutputStream = new FileOutputStream(filename);
            objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(this);
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }finally
        {
            if(fileOutputStream != null)
            {
                try
                {
                    fileOutputStream.close();
                }catch(IOException e2)
                {
                    e2.printStackTrace();
                }
            }

            if(objectOutputStream != null)
            {
                try
                {
                    objectOutputStream.close();
                }catch(IOException ex1)
                {
                    ex1.printStackTrace();
                }
            }
        }

    }

    public static SaveState loadFromFile(String filename)
    {
        SaveState saveState = null;
        FileInputStream fileInputStream = null;
        ObjectInputStream objectInputStream = null;
        try
        {
            File file = new File(filename);
            if(file.exists())
            {
                fileInputStream = new FileInputStream(filename);
                objectInputStream = new ObjectInputStream(fileInputStream);
                saveState = (SaveState) objectInputStream.readObject();
            }
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }finally
        {
            if(fileInputStream != null)
            {
                try
                {
                    fileInputStream.close();
                }catch(IOException e2)
                {
                    e2.printStackTrace();
                }
            }

            if(objectInputStream != null)
            {
                try
                {
                    objectInputStream.close();
                }catch(IOException ex1)
                {
                    ex1.printStackTrace();
                }
            }
        }

        return saveState;
    }
}
