package com.lti.service;

import java.util.List;

import com.lti.service.bo.InviteCode;

public interface InviteManager {
	public Boolean insert(InviteCode inviteCode);
	public InviteCode getInviteId(String invitedCode);
	public InviteCode get(long id);
	public List<InviteCode> getAll();
	public void delById(long id);
}
