# Insight Hosts File Importer

## Introduction
This year I attended the Riada Innovation Days 2019 in Stockholm. It was a 2 days workshop and one sesssion was "The extra mile".
A part of the session was to create your own Import Type. The example reads the local /etc/hosts file and put each ip address and domain name as an object into Insight.

As the example code is still not available and the documentation and examples are still very rare I started to re-create it from the compiled plugin to learn it for my own needs :)

So there are still things that I want to improve. But it worked and 

## Available Documentation

- [Create your own Import Type](https://documentation.riada.io/display/INSSERV/Create+your+own+Import+type+module)

## Available Example

- [Insight Tempo Integration](https://bitbucket.org/mathiasedblom/insight-import-module-tempo-account)

## Coding "Order"
1. Create Atlassian Jira Plugin project
2. Add Insight Maven Dependencies
3. Add Insight Descriptor and Configuration Dialog
4. Add Pre-defined Insight Object Structure and Attributes
5. Add Pre-defined Insight Import Structure
6. Add Insight Importer Logic to read external data source and store it as objects