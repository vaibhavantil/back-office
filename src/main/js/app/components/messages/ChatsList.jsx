import React from 'react';
import styled from 'styled-components';
import { Link } from 'react-router-dom';
import { List, Input } from 'semantic-ui-react';

const ListContainer = styled.div`
    width: 500px;
    border: solid 1px #dfe0e0;
    border-radius: 5px;
    padding: 20px 10px;
    margin-top: 30px;
`;

const Chats = ({ chats }) => (
    <ListContainer>
        <List celled selection verticalAlign="middle" size="big">
            {chats.length ? (
                chats.map(item => (
                    <List.Item key={item.hid}>
                        <Link to={`/messages/${item.hid}`} replace>
                            <List.Content>
                                <List.Header>{item.name}</List.Header>
                            </List.Content>
                        </Link>
                    </List.Item>
                ))
            ) : (
                <h2>Not found</h2>
            )}
        </List>
    </ListContainer>
);

export default class ChatsList extends React.Component {
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
        this.props.search(this.props.client.token, this.state.value);
    }

    componentWillReceiveProps(newProps) {
        if (newProps.chats.list.length) {
            this.setState({
                results: newProps.chats.list,
                isLoading: newProps.chats.requesting,
                value: ''
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
                <Chats chats={this.state.results} />
            </React.Fragment>
        );
    }
}
