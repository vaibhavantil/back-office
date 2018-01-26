import React from 'react';
import UsersList from 'components/users-list/UsersList';
import { Input } from 'semantic-ui-react';

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
            this.props.search(this.props.client.token, this.state.value);
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
        return (
            <React.Fragment>
                <Input
                    loading={this.state.isLoading}
                    placeholder="Search..."
                    iconPosition="left"
                    style={{ marginTop: '30px' }}
                    onKeyPress={this.handleSearchChange}
                    action={{ icon: 'search', onClick: this.searchRequest }}
                />
                <UsersList users={this.state.results} />
            </React.Fragment>
        );
    }
}
