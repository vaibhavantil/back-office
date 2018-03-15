import React from 'react';
import PropTypes from 'prop-types';
import styled from 'styled-components';
import { Tab, Header } from 'semantic-ui-react';
import { subscribe, reconnect } from 'app/lib/sockets/chat';
import { disconnect } from 'sockets';
import { memberPagePanes } from './panes';

const UserDetailsHeader = styled.div`
    display: flex;
    flex-direction: column;
    justify-content: flex-start;
    width: 700px;
`;

export default class Chat extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            socket: null,
            subscription: null
        };
    }

    addMessageHandler = (message, messageType) => {
        const { socket } = this.state;
        const { addMessage, match } = this.props;
        if (socket) addMessage(message, messageType, match.params.id, socket);
    };

    subscribeSocket = () => {
        const { messageReceived, match, messages, errorReceived } = this.props;

        const { stompClient, subscription } = subscribe(
            { messageReceived, errorReceived },
            match.params.id,
            messages.activeConnection
        );
        return { stompClient, subscription };
    };

    reconnectSocket = () => {
        const {
            messageReceived,
            match,
            setActiveConnection,
            errorReceived
        } = this.props;

        reconnect({ messageReceived, errorReceived }, match.params.id).then(
            reslut => {
                const { stompClient, subscription } = reslut;
                this.setState({ socket: stompClient, subscription });
                setActiveConnection(stompClient);
            }
        );
    };

    getChatTitle = user =>
        `Member: ${
            user && (user.firstName || user.lastName)
                ? user.firstName + ' ' + (user.lastName || '')
                : ''
        }`;

    componentDidMount() {
        const { match, userRequest } = this.props;
        const { stompClient, subscription } = this.subscribeSocket();

        if (!stompClient) {
            this.reconnectSocket();
        }
        this.setState({ socket: stompClient, subscription });
        userRequest(match.params.id);
    }

    componentWillUnmount() {
        const { subscription } = this.state;
        disconnect(null, subscription);
        this.props.clearMessagesList();
    }

    render() {
        const { props, addMessageHandler } = this;
        const panes = memberPagePanes(
            props,
            { addMessageHandler, claimsByUser: props.claimsByUser },
            this.state.socket
        );
        // eslint-disable-next-line no-undef
        const showChatTab = !!document.referrer.indexOf('questions') + 1;
        return (
            <React.Fragment>
                <UserDetailsHeader>
                    <Header size="huge">{this.getChatTitle(props.messages.user)}</Header>
                </UserDetailsHeader>
                <Tab
                    panes={panes}
                    renderActiveOnly={true}
                    defaultActiveIndex={showChatTab ? 1 : 0}
                />
            </React.Fragment>
        );
    }
}

Chat.propTypes = {
    messageReceived: PropTypes.func.isRequired,
    match: PropTypes.object.isRequired,
    messages: PropTypes.object.isRequired,
    errorReceived: PropTypes.func,
    addMessage: PropTypes.func.isRequired,
    setActiveConnection: PropTypes.func.isRequired,
    userRequest: PropTypes.func.isRequired,
    error: PropTypes.object,
    clearMessagesList: PropTypes.func.isRequired,
    claimsByUser: PropTypes.func.isRequired,
    userClaims: PropTypes.array
};
