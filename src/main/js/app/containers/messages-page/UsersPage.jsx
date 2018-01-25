import React from 'react';
import styled from 'styled-components';
import { connect } from 'react-redux';
import { withRouter } from 'react-router';
import actions from 'app/store/actions';
import { checkAuthorization } from 'app/lib/checkAuth';
import Users from 'components/users/Users';
import { Link } from 'react-router-dom';
import { Icon } from 'semantic-ui-react';
import { Header } from 'components/chat/Chat';
import * as sockets from 'app/lib/sockets';

const ChatsListPage = styled.div`
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: flex-start;
    max-width: 500px;
    margin: 0 auto;
`;

class UsersPage extends React.Component {
    constructor(props) {
        super(props);
    }

    componentDidMount() {
        const {
            client,
            setClient,
            chatsRequest,
            setActiveConnection,
            messages
        } = this.props;

        checkAuthorization(null, setClient);
        chatsRequest(client.token);
        if (!messages.activeConnection) {
            sockets.connect().then(stompClient => {
                setActiveConnection(stompClient);
            });
        }
    }

    render() {
        const { chats, searchChatRequest, client } = this.props;
        return (
            <ChatsListPage>
                <Header>Chats List</Header>
                <Link to="/assets">
                    <Icon name="arrow left" /> Back
                </Link>
                <Users
                    chats={chats}
                    search={searchChatRequest}
                    client={client}
                />
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
        ...actions.chatUserActions,
        ...actions.messagesActions
    })(UsersPage)
);
