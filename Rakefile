desc "Watch both main and ie compass projects"
task :compile => [:main, :ie] do
    puts "Watching both main and ie compass projects"
end

desc "Watch main compass project"
task :main do
    sh %(cd  CQFiles/CruOrgApp/@JCR_ROOT/apps/CruOrgApp/static/sassfiles/scss-main/; nohup sh Watchfile &)
end

desc "Watch ie compass project"
task :ie do
    sh %(cd CQFiles/CruOrgApp/@JCR_ROOT/apps/CruOrgApp/static/sassfiles/scss-ie/; nohup sh Watchfile &)
end

desc "Watch compass output in console"
task :watch do
    if system 'which -s multitail'
      sh %(multitail -i CQFiles/CruOrgApp/@JCR_ROOT/apps/CruOrgApp/static/sassfiles/scss-main/nohup.out -i CQFiles/CruOrgApp/@JCR_ROOT/apps/CruOrgApp/static/sassfiles/scss-ie/nohup.out)
    elsif system 'which -s brew'
      puts 'Please install multitool'
      puts 'Run: brew install multitool'
    else
      puts 'You need to install Homebrew'
      puts 'Run: ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)"'
    end
end

desc "Find running compass processes"
task :processes do
  sh %(ps aux | grep compass)
end

# TODO Capture pid for each compass process and output to `pid` file
# TODO Add OS X Notifications
# TODO Add a Gemfile to CruOrg so users just have to `bundle install` to get the gems they need