package com.antonfagerberg.instagram.responses

case class Relationship  (
  outgoing_status: String,
  incoming_status: String
) {
  lazy val outgoingStatus = outgoing_status
  lazy val incomingStatus = incoming_status
}