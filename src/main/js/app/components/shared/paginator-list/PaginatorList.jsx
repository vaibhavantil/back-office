import React from 'react';
import PropTypes from 'prop-types';
import { Table } from 'semantic-ui-react';
import styled from 'styled-components';
import Pagination from '../pagination/Pagination';

const PaginatorContainer = styled.div`
    display: flex;
    justify-content: center;
    width: 100%;
`;

export default class PaginatorList extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            activeList: []
        };
    }

    onChangePage = activeList => {
        this.setState({ activeList });
    };

    render() {
        const { activeList } = this.state;
        const { list, itemContent, pageSize, tableHeader } = this.props;
        return (
            <React.Fragment>
                <Table celled selectable>
                    {tableHeader && tableHeader}
                    {activeList.length ? (
                        <Table.Body>
                            {activeList.map(item => (
                                <React.Fragment key={item.hid || item.id}>
                                    {itemContent(item)}
                                </React.Fragment>
                            ))}
                        </Table.Body>
                    ) : null}
                </Table>
                <PaginatorContainer>
                    <Pagination
                        items={list}
                        onChangePage={this.onChangePage}
                        pageSize={pageSize || 6}
                    />
                </PaginatorContainer>
            </React.Fragment>
        );
    }
}

PaginatorList.propTypes = {
    list: PropTypes.array.isRequired,
    itemContent: PropTypes.func.isRequired,
    tableHeader: PropTypes.object,
    pageSize: PropTypes.number
};
