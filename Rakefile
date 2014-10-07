namespace :compile do
    desc "compile main + ie projects and guard files"
    task :all do
        Rake::Task['compile:main'].execute
        Rake::Task['compile:ie'].execute
        Rake::Task['guard'].execute
    end

    desc "compile main project"
    task :main do
        sh %(cd CQFiles/CruOrgApp/@JCR_ROOT/apps/CruOrgApp/static/sassfiles/scss-main/; nohup compass watch --time &)
    end

    desc "compile ie project"
    task :ie do
        sh %(cd CQFiles/CruOrgApp/@JCR_ROOT/apps/CruOrgApp/static/sassfiles/scss-ie/; nohup compass watch --time &)
    end
end

namespace :compile do
    namespace :once do
        desc "compile main project once"
        task :main do
            sh %(cd CQFiles/CruOrgApp/@JCR_ROOT/apps/CruOrgApp/static/sassfiles/scss-main/; compass compile --time)
        end

        desc "compile ie project once"
        task :ie do
            sh %(cd CQFiles/CruOrgApp/@JCR_ROOT/apps/CruOrgApp/static/sassfiles/scss-ie/; compass compile --time)
        end
    end
end

desc "watch compass output in console"
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

desc "find running compass processes"
task :processes do
  puts processes = `ps -ef | grep -v grep | grep CruOrg | awk '{print $2,$8}'`
end

desc "watch files for changes in scss-main and copy them to scss-ie"
task :guard do
    sh %(bundle exec guard)
end

# TODO Iterate through and kill all compass processes
# TODO Upgrade Guardfile w/ guard-compass