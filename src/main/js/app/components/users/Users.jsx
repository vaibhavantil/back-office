import React from 'react';
import { Input } from 'semantic-ui-react';
import UsersList from 'components/users-list/UsersList';

export default class Users extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            isLoading: false,
            results: [],
            value: ''
        };
        this.handleSearchChange = this.handleSearchChange.bind(this);
        this.searchRequest = this.searchRequest.bind(this);
    }

    handleSearchChange(e) {
        const keyValue = e.key;
        this.setState({ value: e.target.value }, () => {
            if (keyValue === 'Enter') this.searchRequest();
        });
    }

    searchRequest() {
        this.setState({ isLoading: true });
        if (this.state.value) {
            this.props.searchUserRequest(
                this.props.client.token,
                this.state.value
            );
        }
    }

    componentDidMount() {
        const { client, usersRequest } = this.props;
        usersRequest(client.token);
    }

    componentWillReceiveProps(newProps) {
        if (newProps.users.list.length) {
            this.setState({
                results: newProps.users.list,
                isLoading: newProps.users.requesting
            });
        }
    }

    render() {
        const {
            messages,
            newMessagesReceived,
            setActiveConnection,
            client
        } = this.props;
        const { isLoading, results } = this.state;
        return (
            <React.Fragment>
                <Input
                    loading={isLoading}
                    placeholder="Search..."
                    iconPosition="left"
                    style={{ marginTop: '30px' }}
                    onKeyPress={this.handleSearchChange}
                    action={{ icon: 'search', onClick: this.searchRequest }}
                />
                <UsersList
                    users={results}
                    messages={messages}
                    newMessagesReceived={newMessagesReceived}
                    setActiveConnection={setActiveConnection}
                    client={client}
                />
            </React.Fragment>
        );
    }
}
