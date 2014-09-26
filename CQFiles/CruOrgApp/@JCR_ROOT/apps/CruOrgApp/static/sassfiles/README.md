Steps to compiling SASS on your local machine:

    Install Compass

        1. Open Terminal
        2. Run: `gem update --system`
        3. Run: `bundle install`
		4. Run: `brew install multitail`
			- install Homebrew if needed, run: `ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)`"


    Run Rake Commands

        1. Open Terminal
        2. Change the Directory to your main CruOrg repository (e.g. `cd /Users/mattgasior/GitHub/CruOrg`)
        3. Run Rake Commands
            - `rake compile` [compiles all .scss files]
            - `rake main` [compiles just main stylesheet]
            - `rake ie` [compiles just ie stylesheet]
            - `rake watch` [watches for changes to .scss files and compiles – cmd+c stops the process]
			- `rake processes` [lists the running compass processes so you can kill them - `kill -9 <process number>`]