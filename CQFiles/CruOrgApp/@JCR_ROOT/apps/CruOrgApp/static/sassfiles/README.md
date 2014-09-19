Steps to compiling SASS on your local machine:

    Install Compass

        1. Open Terminal
        2. Run: gem update --system
        3. Run: gem install compass


    Run Rake Commands

        1. Open Terminal
        2. Change the Directory to your main CruOrg repository (e.g. cd /Users/mattgasior/GitHub/CruOrg)
        3. Run Rake Commands
            - rake watch [compiles all .scss files]
            - rake main [compiles just main stylesheet]
            - rake ie [compiles just ie stylesheet]
            - rake console [watches for changes to .scss files and compiles – cmd+c stops the process]