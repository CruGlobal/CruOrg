desc "Watch both main and ie compass projects"
task :watch => [:main, :ie] do
    puts "Watching both main and ie compass projects"
end

desc "Watch main compass project"
task :main do
    sh %(cd  ~/Dev/CruOrg/CQFiles/CruOrgApp/@JCR_ROOT/apps/CruOrgApp/static/sassfiles/scss-main/; nohup sh Watchfile &)
end

desc "Watch ie compass project"
task :ie do
    sh %(cd ~/Dev/CruOrg/CQFiles/CruOrgApp/@JCR_ROOT/apps/CruOrgApp/static/sassfiles/scss-ie/; nohup sh Watchfile &)
end

desc "Watch compass output in console"
task :console do
    sh %(tail -f ~/Dev/CruOrg/CQFiles/CruOrgApp/@JCR_ROOT/apps/CruOrgApp/static/sassfiles/scss-main/nohup.out ~/Dev/CruOrg/CQFiles/CruOrgApp/@JCR_ROOT/apps/CruOrgApp/static/sassfiles/scss-ie/nohup.out)
end

# ensure that each shell script spawns a new process
# tail both files to watch what's going on `tail -f ~/Dev/CruOrg/CQFiles/CruOrgApp/@JCR_ROOT/apps/CruOrgApp/static/sassfiles/scss-main/nohup.out ~/Dev/CruOrg/CQFiles/CruOrgApp/@JCR_ROOT/apps/CruOrgApp/static/sassfiles/scss-ie/nohup.out`
