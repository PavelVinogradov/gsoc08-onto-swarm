# Introduction #

Swarm is a platform for agent-based models ABMs that includes:

  * A conceptual framework for designing, describing, and conducting experiments on ABMs;
  * Software implementing that framework and providing many handy tools; and
  * A community of users and developers that share ideas, software, and experience.

It mostly written in Objective-C but give ability to develop model in Java. In this article i give first introduction in Java Swarm and show how to run one of examples — Heatbugs. Heatbugs — is one of our canonical Swarm demonstrations, an example of how simple agents acting only on local information can produce complex global behavior.

All commands in this article given for Debian GNU/Linux distribution (and must work similar on most other moder Linux distributions). Good article about building and running Swarm on Debian written by Jakson Aquino (but their article don’t contain information about Java Swarm).
Java installation

Currently i don’t have information about supporting GNU Java by Swarm, therefore i use Sun Java SE 1.6.0\_03 as java virtual machine. To install Sun Java on Debian you have two options:
  * Use Debian backports and install sun-java6-jdk
  * Download binary distribution from official Sun Java site and install it manually

# Build Swarm with Java support #
  1. Install the software necessary to download and compile swarm:
```
     sudo apt-get install gobjc gperf libxpm-dev libpng12-dev automake1.9 emacs21-nox libhdf5-serial-dev blt-dev cvs autoconf libtool make xfonts-75dpi xfonts-100dpi
```
  1. Swarm needs automake1.9. Thus, run update-alternatives and select automake1.9 if it was not the default yet (For Debian Etch — automake 1.9 is already default version):
```
     sudo update-alternatives —config automake
```

  1. Now, you can download the swarm source code. There are two options:
    * Paul Johnson’s web site:
```
          wget http://pj.freefaculty.org/Swarm/swarm-2.2.3.tar.gz tar -xzf swarm-2.2.3.tar.gz cd swarm-2.2.3
```
    * The official cvs repository:
```
          cvs -z3 -d:pserver:anonymous@cvs.savannah.nongnu.org:/sources/swarm co swarm cd swarm ./autogen.sh
```
> > I suggest you to use latest version from repository because it contain some new features and many bugfixes.

  1. Then, you can compile swarm with java support:
    * If you install Java from backport package:
```
       ./configure —with-jdkdir=/usr/lib/jvm/java-6-sun —with-gnu-ld —with-tclinclude=/usr/include/tcl8.4/ —with-tclscriptdir=/usr/lib/tcl8.4/ —with-tkscriptdir=/usr/lib/tk8.4/ —with-tcl=/usr/lib/tcl8.4/ —with-tk=/usr/lib/tk8.4 —with-tkinclude=/usr/include/tcl8.4
```
    * If you install Java from binary distribution archive — replace java\_path on real Java installation path:
> > For swarm from cvs
```
       ./configure —with-jdkdir=java_path —with-gnu-ld —with-tclinclude=/usr/include/tcl8.4/ —with-tclscriptdir=/usr/lib/tcl8.4/ —with-tkscriptdir=/usr/lib/tk8.4/ —with-tcl=/usr/lib/tcl8.4/ —with-tk=/usr/lib/tk8.4
       make
       sudo make install
```


> For swarm 2.2.3
```
       ./configure —with-jdkdir=java_path —with-gnu-ld —with-tclincludedir=/usr/include/tcl8.4/ —with-tclscriptdir=/usr/lib/tcl8.4/ —with-tkscriptdir=/usr/lib/tk8.4/ —with-tcldir=/usr/lib/tcl8.4/ —with-tk=/usr/lib/tk8.4
        make 
        sudo make install
```

  1. Swarm is already installed. You should now put this line at the end of your ~/.bashrc, because, by default, the Makefiles that come with swarm applications are configured to look for swarm at /usr, but since we installed it at /usr/local, we need to inform the Makefiles about this:
```
       export SWARMHOME=/usr/local
```

In addition to putting the above line in your ~/.bashrc, copy and paste it in your terminal too. When you logoff and login again the .bashrc will be re-read and the SWARMHOME environment variable automatically will be active.