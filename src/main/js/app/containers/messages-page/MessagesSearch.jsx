import React from 'react';
import styled from 'styled-components';
import { connect } from 'react-redux';
import { withRouter } from 'react-router';
import actions from 'app/store/actions';
import { checkAuthorization } from 'app/lib/checkAuth';
import ChatsList from 'components/messages/ChatsList';
import { Link } from 'react-router-dom';
import { Icon } from 'semantic-ui-react';
import { Header } from 'components/messages/Chat';

const ChatsListPage = styled.div`
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: flex-start;
    max-width: 500px;
    margin: 0 auto;
`;

class MessagesSearch extends React.Component {
    constructor(props) {
        super(props);
    }

    componentDidMount() {
        const { client, setClient } = this.props;
        checkAuthorization(null, setClient);
        this.props.chatsRequest(client.token);
    }

    render() {
        const { chats, searchChatRequest, client } = this.props;
        return (
            <ChatsListPage>
                <Header>Chats List</Header>
                <Link to="/assets">
                    <Icon name="arrow left" /> Back
                </Link>
                <ChatsList chats={chats} search={searchChatRequest} client={client} />
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
