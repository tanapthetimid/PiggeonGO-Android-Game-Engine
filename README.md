ICS Game Design and Programming club made a game that demos this engine: (download here)
https://github.com/TanapTheTimid/PiggeonGO-Android-Game-Engine/raw/master/SpaceShooterGame.apk

# PiggeonGO Android Game Engine
Simple android game engine based on OpenGL backend.

This project is an adaptation of the Piggeon LWJGL game engine for Java with many improvements in performance. It is an implementation of the GLSurfaceView class and GLRenderer class.

# Design
The design philosophy of Piggeon Android is to be easy to use yet extensible. The object-oriented nature of the framework allows this to be achieved while keeping the design relatively simple. 

A very important part of the design of Piggeon Android is the Updatable interface. This interface is implemented by any object in the game that needs to be updated at any point in the game. The GameObject class implements the Updatable interface. This interface allows the game to be broken down into various small parts.

The GameObject class implements both Updatable and Node, meaning that it can be both updated and rendered by the engine.

The core of the engine is broken down into two sections: the game-state updater and the renderer. The core of the engine, which is responsible for both handing game state update calls and OpenGL render calls, is in the class PiggeonGLRenderer. In every frame, the update interface method of each GameObject stored in the current Stage is called and the game-state is updated. After that, each Node, represented in the GameObject, is rendered recursively, the transformations of a parent node being applied to the children node as well. At the bottom of the recursion, each GameObject is rendered with its texture.

# Components
Updatable: an updatable interface that must be implemented by any object that needs to be updated in the game loop or in a GameObject

Node: a node of objects that can contain other nodes. A node can be transformed in various ways. These transformations will be applied to the node itself and all its children. A node is usually used to group objects that move together. A GameObject is itself a node. Putting GameObject(s) with the same texture may increase performance by a large margin in certain use case. 

GameObject: an object that implement both Node and Updatable. It groups the various interfaces that the engine uses into a single object that represents an object inside the game.

Stage: an object used to group game objects together and handle their initialization. A single scene should be grouped into a stage. Using stage(s) to load assets in bulk may increase game performance. Stages are also used for saving functionality. Stage creation is broken into onCreate() callback and onload() callback to separate OpenGL asset loading from GameObject creation. This is to support saving the stage, as it allows the textures to be reloaded without recreating the GameObject and destroying saved data.

SaveState: an object that holds a stage. It can be written to a file and loaded at a later point.

Camera: an object that contains a Node. The camera object can simulate movement of camera by manipulating the Node that is bounded to it.
