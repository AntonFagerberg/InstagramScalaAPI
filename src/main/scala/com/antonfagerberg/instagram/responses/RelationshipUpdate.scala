package com.antonfagerberg.instagram.responses

case class RelationshipUpdate (
  outgoing_status: String
) {
  lazy val outgoingStatus = outgoing_status
}
