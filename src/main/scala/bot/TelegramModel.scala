package bot

case class Chat(id: Long)
case class Message(message_id: Long, chat: Chat, text: Option[String])
case class Update(update_id: Long, message: Option[Message])

case class UpdatesResult(ok: Boolean, result: List[Update])
