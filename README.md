# Contacts

## Table of contents
* [About](#about)
* [Features](#features)
* [Usage](#usage)
* [Installation](#installation)
* [Technologies and tools](#technologies-and-tools)
* [Screenshots](#screenshots)


## About

Contacts is a simple Command Line tool that provides an easy way to store and manage your contacts.

## Features

- Add contacts to the database
- Search for contacts by name, number and email adress
- Edit existing contact's fields
- List all contacts sorted alphabetically
- Import contacts from .vcf files

## Usage

To use this program open it with console and type instructions.

### Adding contact
To add contact type: `add`<br/>
Then you will be asked for contact's field values. Setting name and number field is mandatory.
To skip entering other information press Enter.

### Getting all contacts list
To get all contacts list(name and number) type: `get all`

### Searching for contacts
To search for contact type: `search PATTERN`<br/>
As PATTERN you can use either name, number or email's fragment.

### Importing contacts
To import contacts from vCard (.vcf file) type: `import FILE_NAME`


### Other
`help`   - shows help message<br/>
`exit`   - closes program

## Installation

1. Import this repository to some folder with `git clone https://github.com/adrian-jrczk/Contacts.git`
2. Open this folder and install with `mvn clean install`
3. In `target` folder there will be executable jar file `contacts.jar` which you can move freely and run with `java -jar contacts.jar`

This program also creates .contacts folder inside home directory to store contacts.

## Technologies and tools

- Java 17.0.2
- Maven 3.8.4
- Hibernate 6.0.0
- H2 database 2.1.212
- Log4j 2.17.2
- Lombok 1.18.24
- Libphonenumber 8.12.46
- Ez-vCard 0.11.3

## Screenshots

![screenshot 1](images/screenshot01.png?raw=true "Adding contacts")
***
![screenshot 2](images/screenshot02.png?raw=true "Importing and editing contacts")
***
![screenshot 3](images/screenshot03.png?raw=true "Searching for contacts")
