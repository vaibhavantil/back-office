import React from 'react';
import styled from 'styled-components';
import { connect } from 'react-redux';
import { withRouter } from 'react-router';
import actions from 'app/store/actions';
import { checkAuthorization } from 'app/lib/checkAuth';
import ChatsList from 'components/messages/ChatsList';


const ChatsListPage = styled.div`
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
`

class MessagesSearch extends React.Component {
    constructor(props) {
        super(props);
    }

    componentDidMount() {
        checkAuthorization(null, this.props.setClient);
        this.props.chatsRequest();
    }

    render() {
        return (
            <ChatsListPage>
                <h1>Chats List</h1>
                <ChatsList chats={this.props.chats.list} />
            </ChatsListPage>
        );
    }
}

const mapStateToProps = ({ client, chats, messages }) => ({
    client,
    chats,
    messages
});

export default withRouter(
    connect(mapStateToProps, {
        ...actions.clientActions,
        ...actions.chatUserActions
    })(MessagesSearch)
);
