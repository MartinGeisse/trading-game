BS=~/software/bootstrap
cp bootstrap-variables.less $BS/less/variables.less
HERE=`pwd`
cd $BS
grunt dist
cd $HERE
cp $BS/dist/css/bootstrap.css ./sass/_bootstrap.scss
