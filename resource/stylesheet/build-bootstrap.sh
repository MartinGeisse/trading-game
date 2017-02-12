BS=~/software/bootstrap
cp variables.less $BS/less
HERE=`pwd`
cd $BS
grunt dist
cd $HERE
cp $BS/dist/css/bootstrap.css .
cp $BS/dist/css/bootstrap.min.css .
