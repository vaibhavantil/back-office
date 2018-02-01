import React from 'react';
import { List } from 'semantic-ui-react';
import Pagination from '../pagination/Pagination';
import { ListContainer } from 'components/shared';

export default class PaginatorList extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            activeList: []
        };
        this.onChangePage = this.onChangePage.bind(this);
    }

    onChangePage(activeList) {
        this.setState({ activeList });
    }

    render() {
        const { activeList } = this.state;
        const { list, itemContent } = this.props;
        return (
            <ListContainer>
                <List selection size="big">
                    {activeList.length ? (
                        activeList.map(item => (
                            <List.Item key={item.hid || item.id}>
                                {itemContent(item)}
                            </List.Item>
                        ))
                    ) : (
                        <h2>Not found</h2>
                    )}
                </List>
                <Pagination
                    items={list}
                    onChangePage={this.onChangePage}
                    pageSize={6}
                />
            </ListContainer>
        );
    }
}
