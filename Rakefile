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
    sh %(multitail -i CQFiles/CruOrgApp/@JCR_ROOT/apps/CruOrgApp/static/sassfiles/scss-main/nohup.out -i ~/Dev/CruOrg/CQFiles/CruOrgApp/@JCR_ROOT/apps/CruOrgApp/static/sassfiles/scss-ie/nohup.out)
end

# TODO Capture pid for each compass process and output to `pid` file