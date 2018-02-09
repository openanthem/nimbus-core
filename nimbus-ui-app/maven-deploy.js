'use strict';
var config = require('./maven-config.json');
var maven = require('maven-deploy');
maven.config(config);
//maven.install();
maven.deploy('repo-prod-public', true);