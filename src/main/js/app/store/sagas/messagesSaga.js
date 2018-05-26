import { call, takeEvery, put, takeLatest } from "redux-saga/effects";
import {
  MEMBER_REQUESTING,
  ADD_MESSAGE,
  EDIT_MEMBER_DETAILS
} from "constants/members";
import api from "app/api";
import config from "app/api/config";
import {
  memberRequestSuccess,
  memberRequestError,
  editMemberDetailsSuccess
} from "../actions/messagesActions";
import { showNotification } from "../actions/notificationsActions";

function* memberRequestFlow({ id }) {
  try {
    const member = yield call(api, config.members.findOne, null, id);
    yield put(memberRequestSuccess(member.data));
  } catch (error) {
    yield [
      put(showNotification({ message: error.message, header: "Members" })),
      put(memberRequestError(error))
    ];
  }
}

function* editMemberDetailsFlow({ member }) {
  try {
    const path = `${member.memberId}/edit`;
    yield call(api, config.members.edit, member, path);
    yield put(editMemberDetailsSuccess(member));
  } catch (error) {
    yield [
      put(memberRequestError(error)),
      put(showNotification({ message: error.message, header: "Member" }))
    ];
  }
}

function* messagesWatcher() {
  yield [
    takeEvery(ADD_MESSAGE, ({ message, socket, id }) => {
      socket.send(
        config.ws.send + id,
        {},
        JSON.stringify({
          memberId: id,
          msg: message
        })
      );
    }),
    takeLatest(MEMBER_REQUESTING, memberRequestFlow),
    takeLatest(EDIT_MEMBER_DETAILS, editMemberDetailsFlow)
  ];
}

export default messagesWatcher;
