-- ----------------------------------------------------------------------------
--  PostgreSQL Table Creation
--
--  @author     Andrei I. Holub
--  @created    August 2, 2006
--  @version    $Id:$
-- ----------------------------------------------------------------------------

-- This schema represents an Account History and Contact History.
-- REQUIRES: new_cdb.sql

-- The details of the organization and contact history are listed.
-- The organization history captures the current events of an organization.
-- or a contact. The org_id is set for organization history.
-- The contact_id is set for the contact history.
-- The organization history contains all its contact's histories.

CREATE TABLE history (
  history_id INT AUTO_INCREMENT PRIMARY KEY,
  contact_id INTEGER REFERENCES contact(contact_id),
	org_id INTEGER REFERENCES organization(org_id),
  link_object_id INTEGER NOT NULL,
  link_item_id INTEGER,
  status VARCHAR(255),
  type VARCHAR(255),
  description TEXT,
  level INTEGER DEFAULT 10,
  enabled BOOLEAN DEFAULT true,
  -- record status
  entered TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  enteredby INT NOT NULL REFERENCES `access`(user_id),
  modified TIMESTAMP NULL,
  modifiedby INT NOT NULL REFERENCES `access`(user_id)
);
