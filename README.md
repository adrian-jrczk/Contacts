# Contacts

![screenshot 1](images/screenshot01.png?raw=true "Usage example 1")


## About

Contacts is a simple Command Line tool that provides an easy way to store and manage your contacts.

## Features

- Add contacts to the database
- Search for contacts by name, number and email address
- Edit existing contact's fields
- List all contacts sorted alphabetically
- Import contacts from .vcf files

## Usage

To use this program open it with console and type instructions.

### Adding contact
To add contact type: `add`<br/>
Then you will be asked for contact's field values. Setting name field is mandatory.
To skip entering other information press Enter.

### Getting all contacts list
To get all contacts list type: `get all`

### Accessing and editing contact
To access contact type `access CONTACT_NAME`<br/>
After accessing contact you will see all of its fields.
From access view you can also delete contact, edit its fields, add new numbers and emails.<br/>
To edit, use `edit CONTACT_FIELD ITEM_NUMBER ITEM_FIELD` command.<br/>
To add number or email type `add FIELD_NAME`<br/>
To delete contact type `delete`<br/>
Commands examples:
- `edit number 1 type`
- `edit number number`
- `add email`
- `edit address street`


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

The first time you run this program, it creates .contacts folder (containing database file) inside your home directory.

## Technologies and tools

- Java
- Maven
- Hibernate
- H2 database
- Log4j
- Lombok
- Libphonenumber
- Ez-vCard

## Screenshots

![screenshot 2](images/screenshot02.png?raw=true "Usage example 2")
***
![screenshot 3](images/screenshot03.png?raw=true "Usage example 3")
